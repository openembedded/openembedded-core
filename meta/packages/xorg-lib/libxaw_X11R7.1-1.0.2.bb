require xorg-lib-common.inc

DESCRIPTION = "Xaw is the X Athena Widget Set."

DEPENDS += " xproto virtual/libx11 libxext xextproto libxt libxmu libxpm libxp printproto libxau"
PROVIDES = "xaw"

XORG_PN = "libXaw"

do_stage () {
	autotools_stage_all
	ln -sf libXaw6.so.6 ${STAGING_LIBDIR}/libXaw.so.6
	ln -sf libXaw7.so.7 ${STAGING_LIBDIR}/libXaw.so.7
	ln -sf libXaw7.so.7 ${STAGING_LIBDIR}/libXaw.so
}
