require gnutls.inc

PR = "r0"

SRC_URI += "file://gnutls-openssl.patch \
            file://gnutls-texinfo-euro.patch \
            file://configure-fix.patch"

SRC_URI[md5sum] = "4e1517084018a8b1fdc96daabea40528"
SRC_URI[sha256sum] = "b8bfe36450fe671e99db5ff1e44e6b65fda8a79cacd9e77d550eff7da3745fc8"
