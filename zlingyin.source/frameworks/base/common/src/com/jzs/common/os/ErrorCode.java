package com.jzs.common.os;

public final class ErrorCode {
	
    public final static String asString(int code){
        switch(code){
            case SUCCESS: return "Success";
            case EPERM: return "Operation not permitted";
            case ENOENT: return "No such file or directory";
            case ESRCH: return "No such process";
            case EINTR: return "Interrupted system call";
            case EIO: return "I/O error";
            case ENXIO: return "No such device or address";
            case E2BIG: return "Arg list too long";
            case ENOEXEC: return "Exec format error";
            case EBADF: return "Bad file number";
            case ECHILD: return "No child processes";
            case EAGAIN: return "Try again";
            case ENOMEM: return "Out of memory";
            case EACCES: return "Permission denied";
            case EFAULT: return "Bad address";
            case ENOTBLK: return "Block device required";
            case EBUSY: return "Device or resource busy";
            case EEXIST: return "File exists";
            case EXDEV: return "Cross-device link";
            case ENODEV: return "No such device";
            case ENOTDIR: return "Not a directory";
            case EISDIR: return "Is a directory";
            case EINVAL: return "Invalid argument";
            case ENFILE: return "File table overflow";
            case EMFILE: return "Too many open files";
            case ENOTTY: return "Not a typewriter";
            case ETXTBSY: return "Text file busy";
            case EFBIG: return "File too large";
            case ENOSPC: return "No space left on device";
            case ESPIPE: return "Illegal seek";
            case EROFS: return "Read-only file system";
            case EMLINK: return "Too many links";
            case EPIPE: return "Broken pipe";
            case EDOM: return "Math argument out of domain of func";
            case ERANGE: return "Math result not representable";
            case EDEADLK: return "Resource deadlock would occur";
            case ENAMETOOLONG: return "File name too long";
            case ENOLCK: return "No record locks available";
            case ENOSYS: return "Function not implemented";
            case ENOTEMPTY: return "Directory not empty";
            case ELOOP: return "Too many symbolic links encountered";
            case ENOMSG: return "No message of desired type";
            case EIDRM: return "Identifier removed";
            case ECHRNG: return "Channel number out of range";
            case EL2NSYNC: return "Level 2 not synchronized";
            case EL3HLT: return "Level 3 halted";
            case EL3RST: return "Level 3 reset";
            case ELNRNG: return "Link number out of range";
            case EUNATCH: return "Protocol driver not attached";
            case ENOCSI: return "No CSI structure available";
            case EL2HLT: return "Level 2 halted";
            case EBADE: return "Invalid exchange";
            case EBADR: return "Invalid request descriptor";
            case EXFULL: return "Exchange full";
            case ENOANO: return "No anode";
            case EBADRQC: return "Invalid request code";
            case EBADSLT: return "Invalid slot";
            case EBFONT: return "Bad font file format";
            case ENOSTR: return "Device not a stream";
            case ENODATA: return "No data available";
            case ETIME: return "Timer expired";
            case ENOSR: return "Out of streams resources";
            case ENONET: return "Machine is not on the network";
            case ENOPKG: return "Package not installed";
            case EREMOTE: return "Object is remote";
            case ENOLINK: return "Link has been severed";
            case EADV: return "Advertise error";
            case ESRMNT: return "Srmount error";
            case ECOMM: return "Communication error on send";
            case EPROTO: return "Protocol error";
            case EMULTIHOP: return "Multihop attempted";
            case EDOTDOT: return "RFS specific error";
            case EBADMSG: return "Not a data message";
            case EOVERFLOW: return "Value too large for defined data type";
            case ENOTUNIQ: return "Name not unique on network";
            case EBADFD: return "File descriptor in bad state";
            case EREMCHG: return "Remote address changed";
            case ELIBACC: return "Can not access a needed shared library";
            case ELIBBAD: return "Accessing a corrupted shared library";
            case ELIBSCN: return ".lib section in a.out corrupted";
            case ELIBMAX: return "Attempting to link in too many shared libraries";
            case ELIBEXEC: return "Cannot exec a shared library directly";
            case EILSEQ: return "Illegal byte sequence";
            case ERESTART: return "Interrupted system call should be restarted";
            case ESTRPIPE: return "Streams pipe error";
            case EUSERS: return "Too many users";
            case ENOTSOCK: return "Socket operation on non-socket";
            case EDESTADDRREQ: return "Destination address required";
            case EMSGSIZE: return "Message too long";
            case EPROTOTYPE: return "Protocol wrong type for socket";
            case ENOPROTOOPT: return "Protocol not available";
            case EPROTONOSUPPORT: return "Protocol not supported";
            case ESOCKTNOSUPPORT: return "Socket type not supported";
            case EOPNOTSUPP: return "Operation not supported on transport endpoint";
            case EPFNOSUPPORT: return "Protocol family not supported";
            case EAFNOSUPPORT: return "Address family not supported by protocol";
            case EADDRINUSE: return "Address already in use";
            case EADDRNOTAVAIL: return "Cannot assign requested address";
            case ENETDOWN: return "Network is down";
            case ENETUNREACH: return "Network is unreachable";
            case ENETRESET: return "Network dropped connection because of reset";
            case ECONNABORTED: return "Software caused connection abort";
            case ECONNRESET: return "Connection reset by peer";
            case ENOBUFS: return "No buffer space available";
            case EISCONN: return "Transport endpoint is already connected";
            case ENOTCONN: return "Transport endpoint is not connected";
            case ESHUTDOWN: return "Cannot send after transport endpoint shutdown";
            case ETOOMANYREFS: return "Too many references: cannot splice";
            case ETIMEDOUT: return "Connection timed out";
            case ECONNREFUSED: return "Connection refused";
            case EHOSTDOWN: return "Host is down";
            case EHOSTUNREACH: return "No route to host";
            case EALREADY: return "Operation already in progress";
            case EINPROGRESS: return "Operation now in progress";
            case ESTALE: return "Stale NFS file handle";
            case EUCLEAN: return "Structure needs cleaning";
            case ENOTNAM: return "Not a XENIX named type file";
            case ENAVAIL: return "No XENIX semaphores available";
            case EISNAM: return "Is a named type file";
            case EREMOTEIO: return "Remote I/O error";
            case EDQUOT: return "Quota exceeded";
            case ENOMEDIUM: return "No medium found";
            case EMEDIUMTYPE: return "Wrong medium type";
            default: break;
        }
        return String.format("'%d'-unkown", code);
    }
    
    /**
     * return success
     */
    public final static int SUCCESS = 0;
    
    /**   * Operation not permitted   */
    public final static int EPERM =  -1;
    
    /**   *  No such file or directory    */
    public final static int ENOENT =  -2; 
    
    /**   * No such process    */
    public final static int ESRCH =  -3; 
    
    /**   * Interrupted system call     * */
    public final static int EINTR =  -4; 
    
    /**  I/O error  * */
    public final static int EIO =  -5; 
    
    /**  No such device or address    * */
    public final static int ENXIO =  -6; 
    
    /**  * Arg list too long     * */
    public final static int E2BIG =  -7; 
    
    /**  * Exec format error   * */
    public final static int ENOEXEC =  -8; 
    
    /**  * Bad file number   * */
    public final static int EBADF =  -9; 
    
    /**   * No child processes   * */
    public final static int ECHILD =  -10; 
    
    /**  * Try again   * */
    public final static int EAGAIN =  -11; 
    
    /** * Out of memory   * */
    public final static int ENOMEM =  -12; 
    
    /**  * Permission denied   * */
    public final static int EACCES =  -13; 
    
    /**  * Bad address   * */
    public final static int EFAULT =  -14; 
    
    /** * Block device required   * */
    public final static int ENOTBLK =  -15; 
    
    /** Device or resource busy */
    public final static int EBUSY =  -16; 
    
    /** File exists */
    public final static int EEXIST =  -17; 
    
    /** Cross-device link */
    public final static int EXDEV =  -18; 
    
    /** No such device */
    public final static int ENODEV =  -19; 
    
    /** Not a directory */
    public final static int ENOTDIR =  -20; 
    
    /** Is a directory */
    public final static int EISDIR =  -21; 
    
    /** Invalid argument */
    public final static int EINVAL =  -22; 
    
    /** File table overflow */
    public final static int ENFILE =  -23; 
    
    /** Too many open files */
    public final static int EMFILE =  -24;
    
    /** Not a typewriter */
    public final static int ENOTTY =  -25; 
    
    /** Text file busy */
    public final static int ETXTBSY = -26; 
    
    /** File too large */
    public final static int EFBIG = -27; 
    
    /** No space left on device */
    public final static int ENOSPC = -28; 
    
    /** Illegal seek */
    public final static int ESPIPE = -29; 
    
    /** Read-only file system */
    public final static int EROFS = -30; 
    
    /** Too many links */
    public final static int EMLINK = -31; 
    
    /** Broken pipe */
    public final static int EPIPE = -32; 
    
    /** Math argument out of domain of func */
    public final static int EDOM = -33; 
    
    /** Math result not representable */
    public final static int ERANGE = -34; 
    
    /** Resource deadlock would occur */
    public final static int EDEADLK = -35; 
    
    /** File name too long */
    public final static int ENAMETOOLONG = -36; 
    
    /** No record locks available */
    public final static int ENOLCK = -37; 
    
    /** Function not implemented */
    public final static int ENOSYS = -38; 
    
    /** Directory not empty */
    public final static int ENOTEMPTY = -39; 
    
    /** Too many symbolic links encountered */
    public final static int ELOOP = -40; 
    
    /** Operation would block */
    public final static int EWOULDBLOCK = EAGAIN; 
    
    /** No message of desired type */
    public final static int ENOMSG = -42; 
    
    /** Identifier removed */
    public final static int EIDRM = -43; 
    
    /** Channel number out of range */
    public final static int ECHRNG = -44; 
    
    /** Level 2 not synchronized */
    public final static int EL2NSYNC = -45; 
    
    /** Level 3 halted */
    public final static int EL3HLT = -46; 
    
    /** Level 3 reset */
    public final static int EL3RST = -47; 
    
    /** Link number out of range */
    public final static int ELNRNG = -48; 
    
    /** Protocol driver not attached */
    public final static int EUNATCH = -49; 
    
    /** No CSI structure available */
    public final static int ENOCSI = -50; 
    
    /** Level 2 halted */
    public final static int EL2HLT = -51; 
    
    /** Invalid exchange */
    public final static int EBADE = -52; 
    
    /** Invalid request descriptor */
    public final static int EBADR = -53; 
    
    /** Exchange full */
    public final static int EXFULL = -54; 
    
    /** No anode */
    public final static int ENOANO = -55; 
    
    /** Invalid request code */
    public final static int EBADRQC = -56; 
    
    /** Invalid slot */
    public final static int EBADSLT = -57; 
    
    public final static int EDEADLOCK = EDEADLK;
    
    /** Bad font file format */
    public final static int EBFONT = -59; 
    
    /** Device not a stream */
    public final static int ENOSTR = -60; 
    
    /** No data available */
    public final static int ENODATA = -61; 
    
    /** Timer expired */
    public final static int ETIME = -62; 
    
    /** Out of streams resources */
    public final static int ENOSR = -63; 
    
    /** Machine is not on the network */
    public final static int ENONET = -64; 
    
    /** Package not installed */
    public final static int ENOPKG = -65; 
    
    /** Object is remote */
    public final static int EREMOTE = -66; 
    
    /** Link has been severed */
    public final static int ENOLINK = -67; 
    
    /** Advertise error */
    public final static int EADV = -68; 
    
    /** Srmount error */
    public final static int ESRMNT = -69; 
    
    /** Communication error on send */
    public final static int ECOMM = -70; 
    
    /** Protocol error */
    public final static int EPROTO = -71; 
    
    /** Multihop attempted */
    public final static int EMULTIHOP = -72; 
    
    /** RFS specific error */
    public final static int EDOTDOT = -73; 
    
    /** Not a data message */
    public final static int EBADMSG = -74; 
    
    /** Value too large for defined data type */
    public final static int EOVERFLOW = -75; 
    
    /** Name not unique on network */
    public final static int ENOTUNIQ = -76; 
    
    /** File descriptor in bad state */
    public final static int EBADFD = -77; 
    
    /** Remote address changed */
    public final static int EREMCHG = -78; 
    
    /** Can not access a needed shared library */
    public final static int ELIBACC = -79; 
    
    /** Accessing a corrupted shared library */
    public final static int ELIBBAD = -80; 
    
    /** .lib section in a.out corrupted */
    public final static int ELIBSCN = -81; 
    
    /** Attempting to link in too many shared libraries */
    public final static int ELIBMAX = -82; 
    
    /** Cannot exec a shared library directly */
    public final static int ELIBEXEC = -83; 
    
    /** Illegal byte sequence */
    public final static int EILSEQ = -84; 
    
    /** Interrupted system call should be restarted */
    public final static int ERESTART = -85; 
    
    /** Streams pipe error */
    public final static int ESTRPIPE = -86;
    
    /** Too many users */
    public final static int EUSERS = -87; 
    
    /** Socket operation on non-socket */
    public final static int ENOTSOCK = -88; 
    
    /** Destination address required */
    public final static int EDESTADDRREQ = -89; 
    
    /** Message too long */
    public final static int EMSGSIZE = -90; 
    
    /** Protocol wrong type for socket */
    public final static int EPROTOTYPE = -91; 
    
    /** Protocol not available */
    public final static int ENOPROTOOPT = -92; 
    
    /** Protocol not supported */
    public final static int EPROTONOSUPPORT = -93; 
    
    /** Socket type not supported */
    public final static int ESOCKTNOSUPPORT = -94; 
    
    /** Operation not supported on transport endpoint */
    public final static int EOPNOTSUPP = -95; 
    
    /** Protocol family not supported */
    public final static int EPFNOSUPPORT = -96; 
    
    /** Address family not supported by protocol */
    public final static int EAFNOSUPPORT = -97; 
    
    /** Address already in use */
    public final static int EADDRINUSE = -98; 
    
    /** Cannot assign requested address */
    public final static int EADDRNOTAVAIL = -99; 
    
    /** Network is down */
    public final static int ENETDOWN = -100; 
    
    /** Network is unreachable */
    public final static int ENETUNREACH = -101; 
    
    /** Network dropped connection because of reset */
    public final static int ENETRESET = -102; 
    
    /** Software caused connection abort */
    public final static int ECONNABORTED = -103; 
    
    /** Connection reset by peer */
    public final static int ECONNRESET = -104; 
    
    /** No buffer space available */
    public final static int ENOBUFS = -105; 
    
    /** Transport endpoint is already connected */
    public final static int EISCONN = -106;
    
    /** Transport endpoint is not connected */
    public final static int ENOTCONN = -107; 
    
    /** Cannot send after transport endpoint shutdown */
    public final static int ESHUTDOWN = -108; 
    
    /** Too many references: cannot splice */
    public final static int ETOOMANYREFS = -109; 
    
    /** Connection timed out */
    public final static int ETIMEDOUT = -110; 
    
    /** Connection refused */
    public final static int ECONNREFUSED = -111; 
    
    /** Host is down */
    public final static int EHOSTDOWN = -112;
    
    /** No route to host */
    public final static int EHOSTUNREACH = -113; 
    
    /** Operation already in progress */
    public final static int EALREADY = -114; 
    
    /** Operation now in progress */
    public final static int EINPROGRESS = -115;
    
    /** Stale NFS file handle */
    public final static int ESTALE = -116;
    
    /** Structure needs cleaning */
    public final static int EUCLEAN = -117;
    
    /** Not a XENIX named type file */
    public final static int ENOTNAM = -118;
    
    /** No XENIX semaphores available */
    public final static int ENAVAIL = -119;
    
    /** Is a named type file */
    public final static int EISNAM = -120;
    
    /** Remote I/O error */
    public final static int EREMOTEIO = -121;
    
    /** Quota exceeded */
    public final static int EDQUOT = -122;
    
    /** No medium found */
    public final static int ENOMEDIUM = -123;
    
    /** Wrong medium type */
    public final static int EMEDIUMTYPE = -124;
}
