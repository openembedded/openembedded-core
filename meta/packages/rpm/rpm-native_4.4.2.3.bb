require rpm_${PV}.bb
inherit native

DEPENDS = "beecrypt-native zlib-native file-native popt-native python-native"

export sharedstatedir = "${layout_sharedstatedir}"
export localstatedir = "${layout_localstatedir}"
