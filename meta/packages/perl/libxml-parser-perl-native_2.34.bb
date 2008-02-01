SECTION = "libs"

inherit native

require libxml-parser-perl_${PV}.bb

DEPENDS = "expat-native perl-native"
