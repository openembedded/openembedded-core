"""Helper module for GPG signing"""
import os

import bb
import oe.utils

class LocalSigner(object):
    """Class for handling local (on the build host) signing"""
    def __init__(self, d):
        self.gpg_bin = d.getVar('GPG_BIN', True) or \
                  bb.utils.which(os.getenv('PATH'), 'gpg')
        self.gpg_path = d.getVar('GPG_PATH', True)
        self.rpm_bin = bb.utils.which(os.getenv('PATH'), "rpm")

    def export_pubkey(self, output_file, keyid):
        """Export GPG public key to a file"""
        cmd = '%s --batch --yes --export --armor -o %s ' % \
                (self.gpg_bin, output_file)
        if self.gpg_path:
            cmd += "--homedir %s " % self.gpg_path
        cmd += keyid
        status, output = oe.utils.getstatusoutput(cmd)
        if status:
            raise bb.build.FuncFailed('Failed to export gpg public key (%s): %s' %
                                      (keyid, output))

    def sign_rpms(self, files, keyid, passphrase_file):
        """Sign RPM files"""
        import pexpect

        cmd = self.rpm_bin + " --addsign --define '_gpg_name %s' " % keyid
        if self.gpg_bin:
            cmd += "--define '%%__gpg %s' " % self.gpg_bin
        if self.gpg_path:
            cmd += "--define '_gpg_path %s' " % self.gpg_path
        cmd += ' '.join(files)

        # Need to use pexpect for feeding the passphrase
        proc = pexpect.spawn(cmd)
        try:
            proc.expect_exact('Enter pass phrase:', timeout=15)
            with open(passphrase_file) as fobj:
                proc.sendline(fobj.readline().rstrip('\n'))
            proc.expect(pexpect.EOF, timeout=900)
            proc.close()
        except pexpect.TIMEOUT as err:
            bb.error('rpmsign timeout: %s' % err)
            proc.terminate()
        if os.WEXITSTATUS(proc.status) or not os.WIFEXITED(proc.status):
            bb.error('rpmsign failed: %s' % proc.before.strip())
            raise bb.build.FuncFailed("Failed to sign RPM packages")

    def detach_sign(self, input_file, keyid, passphrase_file, passphrase=None, armor=True):
        """Create a detached signature of a file"""
        import subprocess

        if passphrase_file and passphrase:
            raise Exception("You should use either passphrase_file of passphrase, not both")

        cmd = [self.gpg_bin, '--detach-sign', '--batch', '--no-tty', '--yes',
               '-u', keyid]
        if passphrase_file:
            cmd += ['--passphrase-file', passphrase_file]
        else:
            cmd += ['--passphrase-fd', '0']
        if self.gpg_path:
            cmd += ['--homedir', self.gpg_path]
        if armor:
            cmd += ['--armor']
        cmd.append(input_file)
        job = subprocess.Popen(cmd, stdin=subprocess.PIPE, stdout=subprocess.PIPE,
                               stderr=subprocess.PIPE)
        _, stderr = job.communicate(passphrase)
        if job.returncode:
            raise bb.build.FuncFailed("Failed to create signature for '%s': %s" %
                                      (input_file, stderr))

    def verify(self, sig_file):
        """Verify signature"""
        cmd = self.gpg_bin + " --verify "
        if self.gpg_path:
            cmd += "--homedir %s " % self.gpg_path
        cmd += sig_file
        status, _ = oe.utils.getstatusoutput(cmd)
        ret = False if status else True
        return ret


def get_signer(d, backend):
    """Get signer object for the specified backend"""
    # Use local signing by default
    if backend == 'local':
        return LocalSigner(d)
    else:
        bb.fatal("Unsupported signing backend '%s'" % backend)

