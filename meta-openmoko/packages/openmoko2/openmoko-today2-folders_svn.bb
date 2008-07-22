DESCRIPTION = "The Openmoko Today2 vfolder files"
SECTION = "openmoko/misc"
PV = "0.1.0+svnr${SRCREV}"
PR = "r4"
RCONFLICTS_${PN} = "matchbox-desktop-sato"

inherit openmoko2

FILES_${PN} += "${datadir}"
