SRC_URI = "http://hem.bredband.net/miko22/${P}.tar.gz"

LICENSE = "GPL"
DEPENDS = "gtk+ curl gconf"
HOMEPAGE = "http://hem.bredband.net/miko22/"
DESCRIPTION = "Linux port of the Funambol C++ SyncML client connector." 

inherit autotools pkgconfig
