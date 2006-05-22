include file_${PV}.bb
inherit native

# avoid dependency loop
DEPENDS = ""
PR = "r1"

SRC_URI += "file://native-fix.diff;patch=1"
