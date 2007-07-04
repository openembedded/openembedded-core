require libfontenc_${PV}.bb

DEPENDS = "zlib-native xproto-native"
PE = "1"

XORG_PN = "libfontenc"

inherit native
