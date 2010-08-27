depmod -Ae

(cat /etc/modules; echo; ) |
while read module args
do
	case "$module" in
		\#*|"") continue ;;
	esac
	modprobe $module $args
done
