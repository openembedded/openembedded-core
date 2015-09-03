require gnutls.inc

SRC_URI += "file://correct_rpl_gettimeofday_signature.patch \
            file://configure.ac-fix-sed-command.patch \
            file://use-pkg-config-to-locate-zlib.patch \
            file://eliminated-double-free-CVE-2015-3308.patch \
            file://better-fix-for-double-free-CVE-2015-3308.patch \
           "
SRC_URI[md5sum] = "a37b20b4352a5f542367ded904729c90"
SRC_URI[sha256sum] = "67ab3e92c5d48f3323b897d7c1aa0bb2af6f3a84f5bd9931cda163a7ff32299b"
