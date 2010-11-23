require sudo.inc

PR = "r0"

SRC_URI = "http://ftp.sudo.ws/sudo/dist/sudo-${PV}.tar.gz \
           file://libtool.patch"

SRC_URI[md5sum] = "55d9906535d70a1de347cd3d3550ee87"
SRC_URI[sha256sum] = "38de3c3e08346b2b8dcb3cf7ed0813300d1a1d5696d0f338ea8a4ef232aacf97"

EXTRA_OECONF += " --with-pam=no"
