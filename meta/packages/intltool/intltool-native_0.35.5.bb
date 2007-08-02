require intltool_${PV}.bb

inherit native
DEPENDS = "libxml-parser-perl-native"

export PERL = "/usr/bin/env perl"
SRC_URI_append = " file://intltool-nowarn.patch;patch=1"

DEFAULT_PREFERENCE = "-1"
