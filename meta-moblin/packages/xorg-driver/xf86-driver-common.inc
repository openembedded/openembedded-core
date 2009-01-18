DESCRIPTION = "X driver"
HOMEPAGE = "http://www.x.org"
SECTION = "x11/drivers"
LICENSE = "MIT-X"

PE = "1"
PR = "r0"

DEPENDS = "virtual/xserver-xf86 xproto randrproto"

XORG_PN = "${PN}"
SRC_URI = "${XORG_MIRROR}/individual/driver/${XORG_PN}-${PV}.tar.bz2"

S = "${WORKDIR}/${XORG_PN}-${PV}"

FILES_${PN} += " ${libdir}/xorg/modules/drivers/*.so"
FILES_${PN}-dbg += " ${libdir}/xorg/modules/drivers/.debug"

inherit autotools_stage pkgconfig

# AC_CHECK_FILE doesn't work when cross compiling, so we create a replacement
# macro that simply assumes the test succeeds. 
do_configure_prepend () {
    echo 'AC_DEFUN(CC_AC_CHECK_FILE, $2)' > configure.ac.new
    sed 's/AC_CHECK_FILE/CC_AC_CHECK_FILE/g' configure.ac >> configure.ac.new
    mv configure.ac.new configure.ac
}

