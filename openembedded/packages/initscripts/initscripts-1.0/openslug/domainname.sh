#
# domainname.sh	Set the domainname.
#
test -r /etc/defaultdomain &&
	cat /etc/defaultdomain >/proc/sys/kernel/domainname
