require gnutls.inc

PR = "r1"

SRC_URI += "file://correct_rpl_gettimeofday_signature.patch \
           "
SRC_URI[md5sum] = "b657e3010c10cae2244e7ce79ee3d446"
SRC_URI[sha256sum] = "aef28d629b6ba824bd435f9b23506525e657e3746d4aa021296b13cbaaa6ae71"
