require gnutls.inc

PR = "r0"

SRC_URI += "file://gnutls-openssl.patch \
            file://gnutls-texinfo-euro.patch \
            file://configure-fix.patch"

SRC_URI[md5sum] = "eb0a6d7d3cb9ac684d971c14f9f6d3ba"
SRC_URI[sha256sum] = "d6f846a7064af3ee2c9aebd65dcee76953b767170cbcd719e990ed6b9688a356"
