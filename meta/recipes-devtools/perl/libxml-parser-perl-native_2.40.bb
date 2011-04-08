SECTION = "libs"

require libxml-parser-perl_${PV}.bb

inherit native

DEPENDS = "expat-native perl-native"