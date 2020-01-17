package nl.tudelft.ipv8.peerdiscovery

import androidx.test.ext.junit.runners.AndroidJUnit4
import nl.tudelft.ipv8.Address
import nl.tudelft.ipv8.Peer
import nl.tudelft.ipv8.keyvault.ECCrypto
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NetworkTest {
    @Test
    fun discoverAddress_newPeer_newAddress() {
        val network = Network()
        val peer = Peer(ECCrypto.generateKey())
        val address = Address("1.2.3.4", 1234)
        val serviceId = "123"
        network.discoverAddress(peer, address, serviceId)
        Assert.assertEquals(Pair(peer.mid, serviceId), network.allAddresses[address])
    }

    @Test
    fun getServicesForPeer() {
        val network = Network()
        val peer = Peer(ECCrypto.generateKey())
        val serviceId = "123"
        network.discoverServices(peer, listOf(serviceId))
        val services = network.getServicesForPeer(peer)
        Assert.assertEquals(1, services.size)
        Assert.assertEquals(serviceId, services.first())
    }

    @Test
    fun getPeersForService() {
        val network = Network()
        val peer = Peer(ECCrypto.generateKey())
        val serviceId = "123"
        network.addVerifiedPeer(peer)
        network.discoverServices(peer, listOf(serviceId))
        val peers = network.getPeersForService(serviceId)
        Assert.assertEquals(1, peers.size)
        Assert.assertEquals(peer, peers.first())
    }

    @Test
    fun getVerifiedByPublicKeyBin() {
        val network = Network()
        val peer = Peer(ECCrypto.generateKey())
        network.addVerifiedPeer(peer)
        val verifiedPeer = network.getVerifiedByPublicKeyBin(peer.publicKey.keyToBin())
        Assert.assertEquals(peer.mid, verifiedPeer?.mid)
    }

    @Test
    fun getIntroductionFrom() {
        val network = Network()
        val peer = Peer(ECCrypto.generateKey(), Address("1.2.3.4", 1234))
        val introducedAddress = Address("2.3.4.5", 2345)
        network.discoverAddress(peer, introducedAddress)
        val introductions = network.getIntroductionFrom(peer)
        Assert.assertEquals(1, introductions.size)
        Assert.assertEquals(introducedAddress, introductions[0])
    }

    @Test
    fun removeByAddress() {
        val network = Network()
        val address = Address("1.2.3.4", 1234)
        val peer = Peer(ECCrypto.generateKey(), address)
        network.addVerifiedPeer(peer)
        network.removeByAddress(address)
        val verifiedPeer = network.getVerifiedByAddress(address)
        Assert.assertNull(verifiedPeer)
    }

    @Test
    fun removePeer() {
        val network = Network()
        val address = Address("1.2.3.4", 1234)
        val peer = Peer(ECCrypto.generateKey(), address)
        network.addVerifiedPeer(peer)
        network.removePeer(peer)
        val verifiedPeer = network.getVerifiedByAddress(address)
        Assert.assertNull(verifiedPeer)
    }

    @Test
    fun getWalkableAddresses() {
        val network = Network()

        val noAddresses = network.getWalkableAddresses(null)
        assertEquals(0, noAddresses.size)

        val address = Address("1.2.3.4", 1234)
        val peer = Peer(ECCrypto.generateKey(), address)
        network.addVerifiedPeer(peer)

        // all peers are known
        val noWalkableAddresses = network.getWalkableAddresses(null)
        assertEquals(0, noWalkableAddresses.size)

        val walkableAddresses = network.getWalkableAddresses("abc")
        assertEquals(1, walkableAddresses.size)
    }
}
