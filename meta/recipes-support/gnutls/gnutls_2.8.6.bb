require gnutls.inc

PR = "r0"

SRC_URI += "file://gnutls-openssl.patch \
            file://gnutls-texinfo-euro.patch \
            file://configure-fix.patch"
