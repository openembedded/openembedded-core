LICENSE = "GPL"
DEPENDS = "gtk+ curl gconf"
HOMEPAGE = "http://hem.bredband.net/miko22/"
DESCRIPTION = "Linux port of the Funambol C++ SyncML client connector." 
PR="r1"

SRC_URI = "http://hem.bredband.net/miko22/${P}.tar.gz       \
	   file://syncml-client-pc-in-cross.patch;patch=1 "

inherit autotools pkgconfig

do_stage() {
	autotools_stage_all
}
