require gnutls.inc

PR = "${INC_PR}.0"

SRC_URI += "file://gnutls-openssl.patch \
            file://correct_rpl_gettimeofday_signature.patch \
            file://configure-fix.patch \
           "

SRC_URI[md5sum] = "f1dea97da5d4dcdbc675720c9aad9ee3"
SRC_URI[sha256sum] = "4884eafcc8383ed23209199bbc72ad04f4eb94955a50a594125ff34c6889c564"

python() {
    if not ((d.getVar("INCOMPATIBLE_LICENSE", True) or "").find("GPLv3") != -1):
        # if GPLv3 add patch
        src_uri = (d.getVar("SRC_URI", False) or "").split()
        src_uri.append("file://fix-gettext-version.patch")
        d.setVar("SRC_URI", " ".join(src_uri))
}
