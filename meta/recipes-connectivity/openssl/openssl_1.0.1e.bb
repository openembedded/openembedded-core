require openssl.inc

# For target side versions of openssl enable support for OCF Linux driver
# if they are available.
DEPENDS += "ocf-linux"

CFLAG += "-DHAVE_CRYPTODEV -DUSE_CRYPTODEV_DIGESTS"

PR = "${INC_PR}.2"

LIC_FILES_CHKSUM = "file://LICENSE;md5=f9a8f968107345e0b75aa8c2ecaa7ec8"

export DIRS = "crypto ssl apps engines"
export OE_LDFLAGS="${LDFLAGS}"

SRC_URI += "file://configure-targets.patch \
            file://shared-libs.patch \
            file://oe-ldflags.patch \
            file://engines-install-in-libdir-ssl.patch \
            file://openssl-fix-link.patch \
            file://debian/version-script.patch \
            file://debian/pic.patch \
            file://debian/c_rehash-compat.patch \
            file://debian/ca.patch \
            file://debian/make-targets.patch \
            file://debian/no-rpath.patch \
            file://debian/man-dir.patch \
            file://debian/man-section.patch \
            file://debian/no-symbolic.patch \
            file://debian/debian-targets.patch \
            file://openssl_fix_for_x32.patch \
            file://openssl-fix-doc.patch \
            file://fix-cipher-des-ede3-cfb1.patch \
            file://openssl-avoid-NULL-pointer-dereference-in-EVP_DigestInit_ex.patch \
            file://openssl-avoid-NULL-pointer-dereference-in-dh_pub_encode.patch \
            file://find.pl \
            file://0001-Fix-for-TLS-record-tampering-bug-CVE-2013-4353.patch \
            file://0001-Fix-DTLS-retransmission-from-previous-session.patch \
            file://0001-Use-version-in-SSL_METHOD-not-SSL-structure.patch \
            file://CVE-2014-0160.patch \
            file://openssl-1.0.1e-cve-2014-0195.patch \
            file://openssl-1.0.1e-cve-2014-0198.patch \
            file://openssl-1.0.1e-cve-2014-0221.patch \
            file://openssl-1.0.1e-cve-2014-0224.patch \
            file://openssl-1.0.1e-cve-2014-3470.patch \
            file://openssl-CVE-2010-5298.patch \
	    file://openssl-fix-CVE-2014-3566.patch \
	    file://openssl-fix-CVE-2014-3513.patch \
	    file://openssl-fix-CVE-2014-3567.patch \
	    file://openssl-fix-CVE-2014-3568.patch \
           "

SRC_URI[md5sum] = "66bf6f10f060d561929de96f9dfe5b8c"
SRC_URI[sha256sum] = "f74f15e8c8ff11aa3d5bb5f276d202ec18d7246e95f961db76054199c69c1ae3"

PACKAGES =+ " \
	${PN}-engines \
	${PN}-engines-dbg \
	"

FILES_${PN}-engines = "${libdir}/ssl/engines/*.so ${libdir}/engines"
FILES_${PN}-engines-dbg = "${libdir}/ssl/engines/.debug"

PARALLEL_MAKE = ""
PARALLEL_MAKEINST = ""

do_configure_prepend() {
  cp ${WORKDIR}/find.pl ${S}/util/find.pl
}
