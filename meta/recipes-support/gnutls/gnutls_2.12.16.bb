require gnutls.inc

PR = "${INC_PR}.5"

SRC_URI += "file://gnutls-openssl.patch \
            file://correct_rpl_gettimeofday_signature.patch \
            file://configure-fix.patch \
           "

SRC_URI[md5sum] = "0414bba9760201f27d66787997cbadfb"
SRC_URI[sha256sum] = "d04328857d9e420eca53f7f7fd615bc76d2f5d74984a9b90c0f2c5f7b5bf5b4a"

python() {
    if not ((d.getVar("INCOMPATIBLE_LICENSE", True) or "").find("GPLv3") != -1):
        # if GPLv3 add patch
        src_uri = (d.getVar("SRC_URI", False) or "").split()
        src_uri.append("file://fix-gettext-version.patch")
        d.setVar("SRC_URI", " ".join(src_uri))
}
