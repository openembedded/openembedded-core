require libxdmcp_${PV}.bb

DEPENDS = "xproto-native util-macros-native"
PR = "r1"
PE = "1"

XORG_PN = "libXdmcp"

inherit native
