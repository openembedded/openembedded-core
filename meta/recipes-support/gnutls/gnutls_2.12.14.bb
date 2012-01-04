require gnutls.inc

PR = "${INC_PR}.4"

SRC_URI += "file://gnutls-openssl.patch \
            file://configure-fix.patch"

python() {
    if not ((d.getVar("INCOMPATIBLE_LICENSE", True) or "").find("GPLv3") != -1):
        # if GPLv3 add patch
        src_uri = (d.getVar("SRC_URI", False) or "").split()
        src_uri.append("file://fix-gettext-version.patch")
        d.setVar("SRC_URI", " ".join(src_uri))
}

SRC_URI[md5sum] = "555687a7ffefba0bd9de1e71cb61402c"
SRC_URI[sha256sum] = "5ee72ba6de7a23cf315792561954451e022dac8730149ca95f93c61e95be2ce3"
