require gnutls.inc

PR = "${INC_PR}.0"

SRC_URI += "file://gnutls-openssl.patch \
            file://correct_rpl_gettimeofday_signature.patch \
            file://configure-fix.patch \
           "

SRC_URI[md5sum] = "e9215d00d2930e09bc8f23f02b54f2d1"
SRC_URI[sha256sum] = "780b12e3f05191316390611a3d9d982d29ca3f3abc234517b2fc542064b6f07a"

python() {
    if not ((d.getVar("INCOMPATIBLE_LICENSE", True) or "").find("GPLv3") != -1):
        # if GPLv3 add patch
        src_uri = (d.getVar("SRC_URI", False) or "").split()
        src_uri.append("file://fix-gettext-version.patch")
        d.setVar("SRC_URI", " ".join(src_uri))
}
