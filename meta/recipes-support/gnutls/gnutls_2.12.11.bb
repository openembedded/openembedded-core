require gnutls.inc

PR = "${INC_PR}.0"

SRC_URI += "file://gnutls-openssl.patch \
            file://configure-fix.patch"

SRC_URI[md5sum] = "f08234b64a8025d6d5aa1307868b02ed"
SRC_URI[sha256sum] = "00b58e1aafe99bbd0cb371e7f1df9cc58bf807301cf70a7eebedeee093991360"
