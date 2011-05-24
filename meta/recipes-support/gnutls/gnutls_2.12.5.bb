require gnutls.inc

PR = "r0"

SRC_URI += "file://gnutls-openssl.patch \
            file://configure-fix.patch"

SRC_URI[md5sum] = "2d0bd5ae11534074fcd78da6ea384e64"
SRC_URI[sha256sum] = "bf263880f327ac34a561d8e66b5a729cbe33eea56728bfed3406ff2898448b60"
