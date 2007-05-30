SECTION = "libs"

inherit native

require libxml-parser-perl_${PV}.bb

DEPENDS = "perl-native expat-native"
EXTRA_CPANFLAGS += " EXPATINCPATH='${STAGING_INCDIR}' EXPATLIBPATH='${STAGING_LIBDIR}'"
