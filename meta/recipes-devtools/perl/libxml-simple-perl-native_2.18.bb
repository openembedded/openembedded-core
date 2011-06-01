SECTION = "libs"

inherit native

require libxml-simple-perl_${PV}.bb

DEPENDS += "libxml-parser-perl-native"
