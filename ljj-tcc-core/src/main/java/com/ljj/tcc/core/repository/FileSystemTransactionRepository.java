package com.ljj.tcc.core.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.xa.Xid;

import com.ljj.tcc.core.Transaction;
import com.ljj.tcc.core.repository.helper.TransactionSerializer;
import com.ljj.tcc.core.serializer.KryoPoolSerializer;
import com.ljj.tcc.core.serializer.ObjectSerializer;

/**
 * 事务持久化到文件
 * Version 1.0.0
 * 
 * @author liangjinjing
 * 
 * Date 2019-05-24 11:34
 * 
 */
public class FileSystemTransactionRepository extends CachableTransactionRepository {

    private String rootPath = "/tcc";

    private volatile boolean initialized;

    
    private ObjectSerializer<Transaction> serializer = new KryoPoolSerializer<Transaction>();

    public void setSerializer(ObjectSerializer<Transaction> serializer) {
        this.serializer = serializer;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    protected int doCreate(Transaction transaction) {
        return createFile(transaction);
    }

    @Override
    protected int doUpdate(Transaction transaction) {

        transaction.updateVersion();
        transaction.updateTime();

        writeFile(transaction);
        return 1;
    }

    @Override
    protected int doDelete(Transaction transaction) {

        String fullFileName = getFullFileName(transaction.getXid());
        File file = new File(fullFileName);
        if (file.exists()) {
            return file.delete() ? 1 : 0;
        }
        return 1;
    }

    @Override
    protected Transaction doFindOne(Xid xid) {

        String fullFileName = getFullFileName(xid);
        File file = new File(fullFileName);

        if (file.exists()) {
            return readTransaction(file);
        }

        return null;
    }

    @Override
    protected List<Transaction> doFindAllUnmodifiedSince(LocalDateTime date) {

        List<Transaction> allTransactions = doFindAll();

        List<Transaction> allUnmodifiedSince = new ArrayList<Transaction>();

        for (Transaction transaction : allTransactions) {
            if (transaction.getGmtMidified().compareTo(date) < 0) {
                allUnmodifiedSince.add(transaction);
            }
        }

        return allUnmodifiedSince;
    }


    protected List<Transaction> doFindAll() {

        List<Transaction> transactions = new ArrayList<Transaction>();
        File path = new File(rootPath);
        File[] files = path.listFiles();

        for (File file : files) {
            Transaction transaction = readTransaction(file);
            transactions.add(transaction);
        }

        return transactions;
    }

    private String getFullFileName(Xid xid) {
        return String.format("%s/%s", rootPath, xid);
    }

    private void makeDirIfNecessary() {
        if (!initialized) {
            synchronized (FileSystemTransactionRepository.class) {
                if (!initialized) {
                    File rootPathFile = new File(rootPath);
                    if (!rootPathFile.exists()) {

                        boolean result = rootPathFile.mkdir();

                        if (!result) {
                            throw new TransactionIOException("cannot create root path, the path to create is:" + rootPath);
                        }

                        initialized = true;
                    } else if (!rootPathFile.isDirectory()) {
                        throw new TransactionIOException("rootPath is not directory");
                    }
                }
            }
        }
    }


    private int createFile(Transaction transaction) {
        makeDirIfNecessary();
        String filePath = getFullFileName(transaction.getXid());
        FileChannel channel = null;
        RandomAccessFile raf = null;
        File file = null;
        byte[] content = TransactionSerializer.serialize(serializer, transaction);
        try {
            file = new File(filePath);
            boolean result = file.createNewFile();
            if (!result) {
                return 0;
            }
            raf = new RandomAccessFile(file, "rw");
            channel = raf.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(content.length);
            buffer.put(content);
            buffer.flip();

            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
            channel.force(true);
            return 1;

        } catch (FileNotFoundException e) {
            throw new TransactionIOException(e);
        } catch (IOException e) {
            throw new TransactionIOException(e);
        } finally {
            if (channel != null && channel.isOpen()) {
                try {
                    channel.close();
                } catch (IOException e) {
                    throw new TransactionIOException(e);
                }
            }
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    throw new TransactionIOException(e);
                }
            }
        }
    }

    private void writeFile(Transaction transaction) {
        makeDirIfNecessary();
        String filePath = getFullFileName(transaction.getXid());
        FileChannel channel = null;
        RandomAccessFile raf = null;
        byte[] content = TransactionSerializer.serialize(serializer, transaction);
        try {
            raf = new RandomAccessFile(filePath, "rw");
            channel = raf.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(content.length);
            buffer.put(content);
            buffer.flip();

            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
            
            channel.force(true);
        } catch (Exception e) {
            throw new TransactionIOException(e);
        } finally {
            
            if (channel != null && channel.isOpen()) {
                try {
                    channel.close();

                } catch (IOException e) {
                    throw new TransactionIOException(e);
                }
            }
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    throw new TransactionIOException(e);
                }
            }
        }
    }
    

    /**
     * 
     * @param file
     * @return
     */
    private Transaction readTransaction(File file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] content = new byte[(int) file.length()];
            fis.read(content);
            return TransactionSerializer.deserialize(serializer, content);
        } catch (Exception e) {
            throw new TransactionIOException(e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    throw new TransactionIOException(e);
                }
            }
        }
    }
}
