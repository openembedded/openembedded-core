require gnutls.inc

SRC_URI += "file://correct_rpl_gettimeofday_signature.patch \
            file://eliminated-double-free-CVE-2015-3308.patch \
            file://better-fix-for-double-free-CVE-2015-3308.patch \
           "

SRC_URI[md5sum] = "1f396dcf3c14ea67de7243821006d1a2"
SRC_URI[sha256sum] = "48f34ae032692c498e782e9f1369506572be40ecf7f3f3604b0b00bad1b10477"
