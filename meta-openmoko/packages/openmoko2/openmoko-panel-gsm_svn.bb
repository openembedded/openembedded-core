DESCRIPTION = "Shows the GSM / GPRS status in the Openmoko panel"
DEPENDS = "libmokogsmd2 libnotify"
PV = "0.1.0+svn${SVNREV}"
PR = "r2"

inherit openmoko-panel-plugin
