require gnutls.inc

PR = "${INC_PR}.0"

SRC_URI += "file://gnutls-openssl.patch \
            file://configure-fix.patch \
            file://fix-gettext-version.patch"

SRC_URI[md5sum] = "555687a7ffefba0bd9de1e71cb61402c"
SRC_URI[sha256sum] = "5ee72ba6de7a23cf315792561954451e022dac8730149ca95f93c61e95be2ce3"
