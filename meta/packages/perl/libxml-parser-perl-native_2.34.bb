SECTION = "libs"
include libxml-parser-perl_${PV}.bb
inherit native
DEPENDS = "perl-native expat-native"
