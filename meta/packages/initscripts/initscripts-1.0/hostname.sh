#
# hostname.sh	Set hostname.
#
# Version:	@(#)hostname.sh  1.10  26-Feb-2001  miquels@cistron.nl
#

if test -f /etc/hostname
then
	hostname -F /etc/hostname
fi

