require sudo.inc

PR = "r0"

SRC_URI = "http://ftp.sudo.ws/sudo/dist/sudo-${PV}.tar.gz \
           file://libtool.patch"

SRC_URI[md5sum] = "1ae12d3d22e7ffedbf2db26f957676f0"
SRC_URI[sha256sum] = "20091ef71018698c674c779f4b57178b2ecb4275fa34909b06219d2688ad14d5"

EXTRA_OECONF += " --with-pam=no"
