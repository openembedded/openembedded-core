require gnutls.inc

PR = "${INC_PR}.0"

SRC_URI += "file://gnutls-openssl.patch \
            file://correct_rpl_gettimeofday_signature.patch \
            file://configure-fix.patch \
           "

SRC_URI[md5sum] = "f08990f1afa4e1d0ee13e64e537c7854"
SRC_URI[sha256sum] = "588ad6b0901c789f2f6afcec88ac4d688801bf1a024c9afa08706bb8d9868bf3"

python() {
    if not ((d.getVar("INCOMPATIBLE_LICENSE", True) or "").find("GPLv3") != -1):
        # if GPLv3 add patch
        src_uri = (d.getVar("SRC_URI", False) or "").split()
        src_uri.append("file://fix-gettext-version.patch")
        d.setVar("SRC_URI", " ".join(src_uri))
}
