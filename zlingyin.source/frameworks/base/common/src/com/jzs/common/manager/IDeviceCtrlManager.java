package com.jzs.common.manager;

import java.io.IOException;
import java.nio.ByteBuffer;

import android.os.ParcelFileDescriptor;

public interface IDeviceCtrlManager {
	public final static String MANAGER_SERVICE = IComplexManager.MANAGER_SERVICE_PREFIX+"DevCtrlMgr";
	
	public final static int O_RDONLY = 0;
	public final static int O_WRONLY = 1;
	public final static int O_RDWR = 2;
	
	public int CODE_IO(int type, int nr);
	public int CODE_IOR(int type, int nr);
	public int CODE_IOW(int type, int nr);
	public int CODE_IOWR(int type, int nr);
	
	public int CODE_IOR_INT(int type, int nr);
	public int CODE_IOR_UNSIGNED_INT(int type, int nr);
	public int CODE_IOR_LONG(int type, int nr);
	public int CODE_IOR_UNSIGNED_LONG(int type, int nr);
	
	public int CODE_IOW_INT(int type, int nr);
	public int CODE_IOW_UNSIGNED_INT(int type, int nr);
	public int CODE_IOW_LONG(int type, int nr);
	public int CODE_IOW_UNSIGNED_LONG(int type, int nr);
	
	public int CODE_IOWR_INT(int type, int nr);
	public int CODE_IOWR_UNSIGNED_INT(int type, int nr);
	public int CODE_IOWR_LONG(int type, int nr);
	public int CODE_IOWR_UNSIGNED_LONG(int type, int nr);
	
	public int open_device(String device);
	public int open_device(String device, int mode);
	public void close_device();
	public int set_ioctl_int(int code, int value);
	public int set_ioctl_int_ptr(int code, int value);
	public int get_ioctl_int(int code);
	public int set_ioctl_string(int code, String value);
	public int get_ioctl_string(int code, byte[] buffer, int length);

	
	/**
     * SerialPort should only be instantiated by SerialManager
     * Speed must be one of 50, 75, 110, 134, 150, 200, 300, 600, 1200, 1800, 2400, 4800, 9600,
     * 19200, 38400, 57600, 115200, 230400, 460800, 500000, 576000, 921600, 1000000, 1152000,
     * 1500000, 2000000, 2500000, 3000000, 3500000, 4000000
     * timeout : 0 for block.
     */
	public ParcelFileDescriptor serialport_open_file(String device, int speed) throws IOException;
	
	/**
     * SerialPort should only be instantiated by SerialManager
     * Speed must be one of 50, 75, 110, 134, 150, 200, 300, 600, 1200, 1800, 2400, 4800, 9600,
     * 19200, 38400, 57600, 115200, 230400, 460800, 500000, 576000, 921600, 1000000, 1152000,
     * 1500000, 2000000, 2500000, 3000000, 3500000, 4000000
     * timeout : 0 for block.
     */
	public ParcelFileDescriptor serialport_open_file(String device, int speed, int timeout) throws IOException;
	
	/**
     * SerialPort should only be instantiated by SerialManager
     * Speed must be one of 50, 75, 110, 134, 150, 200, 300, 600, 1200, 1800, 2400, 4800, 9600,
     * 19200, 38400, 57600, 115200, 230400, 460800, 500000, 576000, 921600, 1000000, 1152000,
     * 1500000, 2000000, 2500000, 3000000, 3500000, 4000000
     * timeout : 0 for block.
     * databits : 7 or 8
     * stopbits : 0 or 1
     * parity : 'n', 'N', 's', 'S', 'o', 'O', 'e', 'E'
     */
	public ParcelFileDescriptor serialport_open_file(String device, int speed, int timeout, int databits, int stopbits, int parity) throws IOException;
	
	/**
     * SerialPort should only be instantiated by SerialManager
     * Speed must be one of 50, 75, 110, 134, 150, 200, 300, 600, 1200, 1800, 2400, 4800, 9600,
     * 19200, 38400, 57600, 115200, 230400, 460800, 500000, 576000, 921600, 1000000, 1152000,
     * 1500000, 2000000, 2500000, 3000000, 3500000, 4000000
     * timeout : 0 for block.
     */
	public int serialport_open(String device, int speed);
	
	/**
     * SerialPort should only be instantiated by SerialManager
     * Speed must be one of 50, 75, 110, 134, 150, 200, 300, 600, 1200, 1800, 2400, 4800, 9600,
     * 19200, 38400, 57600, 115200, 230400, 460800, 500000, 576000, 921600, 1000000, 1152000,
     * 1500000, 2000000, 2500000, 3000000, 3500000, 4000000
     * timeout : 0 for block.
     */
	public int serialport_open(String device, int speed, int timeout);
	
	/**
     * SerialPort should only be instantiated by SerialManager
     * Speed must be one of 50, 75, 110, 134, 150, 200, 300, 600, 1200, 1800, 2400, 4800, 9600,
     * 19200, 38400, 57600, 115200, 230400, 460800, 500000, 576000, 921600, 1000000, 1152000,
     * 1500000, 2000000, 2500000, 3000000, 3500000, 4000000
     * timeout : 0 for block.
     * databits : 7 or 8
     * stopbits : 0 or 1
     * parity : 'n', 'N', 's', 'S', 'o', 'O', 'e', 'E'
     */
	public int serialport_open(String device, int speed, int timeout, int databits, int stopbits, int parity);
	
	
	public void serialport_close();
	
	/**
     * Reads data into the provided buffer
     *
     * @param buffer to read into
     * @return number of bytes read
     */
	public int serialport_read(ByteBuffer buffer) throws IOException;
	
	/**
     * Writes data from provided buffer
     *
     * @param buffer to write
     * @param length number of bytes to write
     */
	public void serialport_write(ByteBuffer buffer, int length) throws IOException;
	
	/**
     * Sends a stream of zero valued bits for 0.25 to 0.5 seconds
     */
	public void serialport_sendBreak();
	
	
	public int setStereoType(int type, int layerid);
	
	public int global_set_ioctl_int(String devname, int io_code, int io_value);
	public int global_set_ioctl_int_ptr(String devname, int io_code, int io_value);
	public int global_get_ioctl_int(String devname, int io_code);
	public int global_set_ioctl_string(String devname, int io_code, String io_value);

	public int nvram_get_lid_by_name(String lidname);
    public byte[] nvram_read_by_lid(int file_lid, int size);
	public int nvram_write_by_lid(int file_lid, byte[] buff);
    public int nvram_write_by_lid(int file_lid, int offset, byte[] buff);
    public NvramFileDesSize nvram_get_file_dessize(int file_lid);

	public static class NvramFileDesSize{
		public int recSize;
		public int recNum;
	}
}
