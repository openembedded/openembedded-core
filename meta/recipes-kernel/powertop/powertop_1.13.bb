SUMMARY = "Power usage tool"
DESCRIPTION = "PowerTOP, a tool that helps you find what software is using the most power."
HOMEPAGE = "http://www.lesswatts.org/"
BUGTRACKER = "http://bugzilla.lesswatts.org/"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

DEPENDS = "virtual/libintl ncurses"

# powertop 1.13 needs lspci
RDEPENDS_${PN} = "pciutils"

PR = "r3"

SRC_URI = "http://www.lesswatts.org/projects/powertop/download/powertop-${PV}.tar.gz \
           file://stub_out_the_ncurses_calls_in_dump_mode.patch \
          "

SRC_URI[md5sum] = "78aa17c8f55178004223bf236654298e"
SRC_URI[sha256sum] = "2bc866089496877dd26d2d316ad5763ab8ecb5e28aefba44bc5d355dcdc58d98"

CFLAGS += "${LDFLAGS}"
EXTRA_OEMAKE = "VERSION=\"${PV}\" EXTRA_LIBS=${EXTRA_LIBS}"

EXTRA_LIBS_libc-uclibc = "-lintl"

inherit update-alternatives
ALTERNATIVE_NAME = "powertop"
ALTERNATIVE_PATH = "${bindir}/powertop"
ALTERNATIVE_LINK = "${base_bindir}/powertop"
ALTERNATIVE_PRIORITY = "100"

do_configure() {
	# We do not build ncurses with wide char support
	sed -i -e 's:lncursesw:lncurses ${EXTRA_LIBS}:g' ${S}/Makefile
}

do_install() {
	oe_runmake install DESTDIR=${D}
}

