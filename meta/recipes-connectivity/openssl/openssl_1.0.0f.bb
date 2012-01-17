require openssl.inc

# For target side versions of openssl enable support for OCF Linux driver
# if they are available.
DEPENDS += "ocf-linux"

CFLAG += "-DHAVE_CRYPTODEV -DUSE_CRYPTODEV_DIGESTS"

PR = "${INC_PR}.0"

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
            file://debian/pkg-config.patch \
            file://debian/no-symbolic.patch \
            file://debian/debian-targets.patch \
            file://openssl_fix_for_x32.patch \
           "

SRC_URI[md5sum] = "e358705fb4a8827b5e9224a73f442025"
SRC_URI[sha256sum] = "faf1eab0ef85fd6c3beca271c356b31b5cc831e2c6b7f23cf672e7ab4680fde1"

PACKAGES =+ " \
	${PN}-engines \
	${PN}-engines-dbg \
	"

FILES_${PN}-engines = "${libdir}/ssl/engines/*.so ${libdir}/engines"
FILES_${PN}-engines-dbg = "${libdir}/ssl/engines/.debug"

PARALLEL_MAKEINST = ""
