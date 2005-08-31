# Now that BitBake/OpenEmbedded uses Quilt by default, you can simply add an
#	inherit patcher
# to one of your config files to let BB/OE use patcher again.

PATCHCLEANCMD = "patcher -B"
PATCHCMD = "patcher -R -p '%s' -n '%s' -i '%s'"
PATCH_DEPENDS = "${@["patcher-native", ""][(bb.data.getVar('PN', d, 1) == 'patcher-native')]}"
