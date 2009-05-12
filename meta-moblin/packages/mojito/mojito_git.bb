
SRC_URI = "git://git.moblin.org/${PN}.git;protocol=git"
PV = "0.0+git${SRCREV}"
PR = "r2"

DEPENDS = "libsoup-2.4 gconf-dbus librest glib-2.0 twitter-glib sqlite3 gnome-keyring"

S = "${WORKDIR}/git"

inherit autotools_stage

FILES_${PN} += "${datadir}/dbus-1/services"
FILES_${PN}-dbg += "${libdir}/mojito/sources/.debug/* ${libdir}/mojito/services/.debug/"

PARALLEL_MAKE = ""

pkg_postinst_${PN} () {
#!/bin/sh -e
if [ "x$D" != "x" ]; then
    exit 1
fi
    
. ${sysconfdir}/init.d/functions
    
gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults --direct --type string -s /apps/mojito/sources/flickr/user 34402200@N07
gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.default  --direct --type string -s /apps/mojito/sources/twitter/user ross@linux.intel.com
gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults --direct --type string -s /apps/mojito/sources/twitter/password password 

}
