LICENSE = "GPL"
SECTION = "x11/base"
DESCRIPTION = "GTK theme engines"
DEPENDS = "gtk+"

DEFAULT_PREFERENCE = "-1"

RDEPENDS_gtk-theme-redmond = "gtk-engine-redmond95"
RDEPENDS_gtk-theme-metal = "gtk-engine-metal"
RDEPENDS_gtk-theme-mist = "gtk-engine-mist"
RDEPENDS_gtk-theme-crux = "gtk-engine-crux-engine"
RDEPENDS_gtk-theme-lighthouseblue = "gtk-engine-lighthouseblue"
RDEPENDS_gtk-theme-thinice = "gtk-engine-thinice"
RDEPENDS_gtk-theme-industrial = "gtk-engine-industrial"
RDEPENDS_gtk-theme-clearlooks = "gtk-engine-clearlooks"

CFLAGS_prepend = "-DHAVE_ANIMATION "

inherit gnome

python populate_packages_prepend() {
	import os.path

	engines_root = os.path.join(bb.data.getVar('libdir', d, 1), "gtk-2.0/2.10.0/engines")
	themes_root = os.path.join(bb.data.getVar('datadir', d, 1), "themes")

	do_split_packages(d, engines_root, '^lib(.*)\.so$', 'gtk-engine-%s', 'GTK %s theme engine', extra_depends='')
	do_split_packages(d, themes_root, '(.*)', 'gtk-theme-%s', 'GTK theme %s', allow_dirs=True, extra_depends='')
}
