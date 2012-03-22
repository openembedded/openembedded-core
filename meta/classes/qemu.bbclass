#
# This class contains functions for recipes that need QEMU or test for its
# existence.
#

def qemu_target_binary(data):
	import bb

	target_arch = data.getVar("TARGET_ARCH", True)
	if target_arch in ("i486", "i586", "i686"):
		target_arch = "i386"
	elif target_arch == "powerpc":
		target_arch = "ppc"

	return "qemu-" + target_arch
