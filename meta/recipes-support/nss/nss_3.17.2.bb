require nss.inc

SRC_URI += "\
    http://ftp.mozilla.org/pub/mozilla.org/security/nss/releases/NSS_3_17_2_RTM/src/${BPN}-${PV}.tar.gz \
"

SRC_URI[md5sum] = "d3edb6f6c3688b2fde67ec9c9a8c1214"
SRC_URI[sha256sum] = "134929e44e44b968a4883f4ee513a71ae45d55b486cee41ee8e26c3cc84dab8b"
