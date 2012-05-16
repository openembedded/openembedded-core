require gnutls.inc

PR = "${INC_PR}.0"

SRC_URI += "file://gnutls-openssl.patch \
            file://correct_rpl_gettimeofday_signature.patch \
            file://configure-fix.patch \
           "

SRC_URI[md5sum] = "14228b34e3d8ed176a617df40693b441"
SRC_URI[sha256sum] = "8f167b39de4e2fddd83f7e29c98ce2e7fc76f4d79ccf4d3d66d1379b971160ec"

python() {
    if not ((d.getVar("INCOMPATIBLE_LICENSE", True) or "").find("GPLv3") != -1):
        # if GPLv3 add patch
        src_uri = (d.getVar("SRC_URI", False) or "").split()
        src_uri.append("file://fix-gettext-version.patch")
        d.setVar("SRC_URI", " ".join(src_uri))
}
