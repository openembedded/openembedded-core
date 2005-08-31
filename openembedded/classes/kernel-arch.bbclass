#
# set the ARCH environment variable for kernel compilation (including
# modules). return value must match one of the architecture directories
# in the kernel source "arch" directory
#

valid_archs = "alpha cris ia64 m68knommu ppc sh \
	       sparc64 x86_64 arm h8300 m32r mips \
	       ppc64 sh64 um arm26 i386 m68k \
	       parisc s390 sparc v850"

def map_kernel_arch(a, d):
	import bb, re

	valid_archs = bb.data.getVar('valid_archs', d, 1).split()

	if   re.match('(i.86|athlon)$', a):	return 'i386'
	elif re.match('arm26$', a):		return 'arm26'
	elif re.match('armeb$', a):		return 'arm'
	elif re.match('powerpc$', a):		return 'ppc'
	elif re.match('mipsel$', a):		return 'mips'
	elif a in valid_archs:			return a
	else:
		bb.error("cannot map '%s' to a linux kernel architecture" % a)

export ARCH = "${@map_kernel_arch(bb.data.getVar('TARGET_ARCH', d, 1), d)}"
