SUMMARY = "Automated text and program generation tool"
DESCRIPTION = "AutoGen is a tool designed to simplify the creation and\
 maintenance of programs that contain large amounts of repetitious text.\
 It is especially valuable in programs that have several blocks of text\
 that must be kept synchronized."
HOMEPAGE = "http://www.gnu.org/software/autogen/"
SECTION = "devel"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

SRC_URI = "${GNU_MIRROR}/autogen/rel${PV}/autogen-${PV}.tar.gz \
           file://guile.patch \
           file://increase-timeout-limit.patch \
           file://mk-tpl-config.sh-force-exit-value-to-be-0-in-subproc.patch"

SRC_URI[md5sum] = "0fb6b003423e004d94e0119c2390078f"
SRC_URI[sha256sum] = "73d05a689105eb9b8be54f32498c99ddbd360776fc61cf45be6a2a4eb4a40039"

DEPENDS = "guile-native libtool-native libxml2-native"

inherit autotools texinfo native pkgconfig

# autogen-native links against libguile which may have been relocated with sstate
# these environment variables ensure there isn't a relocation issue
export GUILE_LOAD_PATH = "${STAGING_DATADIR_NATIVE}/guile/2.0"
export GUILE_LOAD_COMPILED_PATH = "${STAGING_LIBDIR_NATIVE}/guile/2.0/ccache"

do_install_append () {
	create_wrapper ${D}/${bindir}/autogen \
		GUILE_LOAD_PATH=${STAGING_DATADIR_NATIVE}/guile/2.0 \
		GUILE_LOAD_COMPILED_PATH=${STAGING_LIBDIR_NATIVE}/guile/2.0/ccache
}
