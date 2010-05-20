#
# set the ARCH environment variable for kernel compilation (including
# modules). return value must match one of the architecture directories
# in the kernel source "arch" directory
#

valid_archs = "alpha cris ia64 \
               x86_64 i386 x86 \
               m68knommu m68k ppc powerpc ppc64  \
	       sparc sparc64 \
               arm  arm26 \
               m32r mips \
	       sh sh64 um h8300   \
	       parisc s390  v850 \
	       avr32 blackfin"

def map_kernel_arch(a, d):
	import re

	valid_archs = bb.data.getVar('valid_archs', d, 1).split()

	if   re.match('(i.86|athlon)$', a):	return 'i386'
	elif re.match('arm26$', a):		return 'arm26'
	elif re.match('armeb$', a):		return 'arm'
	elif re.match('mipsel$', a):		return 'mips'
	elif re.match('sh(3|4)$', a):		return 'sh'
	elif re.match('bfin', a):               return 'blackfin'
        elif a in valid_archs:			return a
	else:
		bb.error("cannot map '%s' to a linux kernel architecture" % a)

export ARCH = "${@map_kernel_arch(bb.data.getVar('TARGET_ARCH', d, 1), d)}"

def map_uboot_arch(a, d):
	import re

	if   re.match('powerpc$', a): return 'ppc'
	elif re.match('i.86$', a): return 'x86'
	return a

export UBOOT_ARCH = "${@map_uboot_arch(bb.data.getVar('ARCH', d, 1), d)}"

