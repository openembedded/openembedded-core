require nss.inc

SRC_URI += "\
    http://ftp.mozilla.org/pub/mozilla.org/security/nss/releases/NSS_3_17_RTM/src/${BPN}-${PV}.tar.gz \
"

SRC_URI[md5sum] = "081dd99afa12af589c09e2d7cb5f5c6d"
SRC_URI[sha256sum] = "3b1abcd8f89211dda2cc739bfa76552d080f7ea80482ef2727b006548a7f0c81"
