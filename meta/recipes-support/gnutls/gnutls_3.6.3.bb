require gnutls.inc

SRC_URI += "file://0001-configure.ac-fix-sed-command.patch \
            file://arm_eabi.patch \
           "

SRC_URI[md5sum] = "d3b1b05c2546b80832101a423a80faf8"
SRC_URI[sha256sum] = "ed642b66a4ecf4851ab2d809cd1475c297b6201d8e8bd14b4d1c08b53ffca993"

BBCLASSEXTEND = "native nativesdk"
