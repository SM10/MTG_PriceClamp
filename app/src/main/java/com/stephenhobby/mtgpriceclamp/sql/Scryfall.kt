package com.stephenhobby.mtgpriceclamp.sql

import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.net.URL
import com.stephenhobby.mtgpriceclamp.datatype.Card

public class ScryfallApi(val callback : Callback){
    private val TAG = "ScryfallApi"
    interface Callback{
        abstract fun onQueryResult(cardlist : ArrayList<Card>);
        abstract fun onQueryError(error: Error)
    }

    fun UserQuery(query : String) {
        try {
            val requestresult: JSONObject =
                Connect(query, "https://api.scryfall.com/cards/search?q=" + query);
            val cardArray: JSONArray = requestresult.getJSONArray("data")
            val cardList = ArrayList<Card>();
            for (counter in 0..cardArray.length() - 1) {
                cardList.add(ToCard(cardArray.getJSONObject(counter)))
            }
            callback.onQueryResult(cardList)
        }catch(e : Error){
            callback.onQueryError(e)
        }
    }

    fun ToCard(jsonCard: JSONObject) : Card{
        val card = Card(
            jsonCard.getString("id"),
            jsonCard.getString("oracle_id"),
            jsonCard.getInt("tcgplayer_id"),
            jsonCard.getInt("cardmarket_id"),
            jsonCard.getString("name")
        )
        card.prices = getPrices(jsonCard)

        return card;
    }

    private fun Connect(query : String, path : String) : JSONObject{
        val out = ByteArrayOutputStream();
        try {
            runBlocking {
                val job = launch(Dispatchers.IO) {
                    val url = URL(path)

                    val connection = url.openConnection()
                    val inputstream = connection.getInputStream()
                    var buffer = ByteArray(1024)

                    var readBytes = inputstream.read(buffer, 0, buffer.size)
                    while (readBytes > 0) {
                        out.write(buffer, 0, readBytes)
                        readBytes = inputstream.read(buffer, 0, buffer.size)
                    }
                }
                job.join()
            }
        }catch(e : Exception){
            throw e;
        }

        val jsonString = out.toString("UTF-8")
        println(jsonString)
        return JSONObject(jsonString);
    }

    private fun toCharArray(jsonArray : JSONArray) : Array<Char>{
        val colors : Array<Char> = Array(jsonArray.length()) {'0'}
        for (counter in 0 .. jsonArray.length() - 1){
            colors[counter] = jsonArray[counter].toString()[counter]
        }
        return colors
    }

    private fun toStringArray(jsonArray : JSONArray) : Array<String>{
        val array : Array<String> = Array(jsonArray.length()){""}
        for(counter in 0 .. jsonArray.length() - 1){
            array[counter] = jsonArray[counter] as String
        }
        return array;
    }

    //prepares image uris to be loaded into Card
    private fun getImageUris(jsonCard: JSONObject) : HashMap<String, Uri> {
        //prepares image uris to be loaded into Card
        val imageuris: HashMap<String, Uri> = HashMap<String, Uri>()
        imageuris.put("small", Uri.parse(jsonCard.getJSONObject("image_uris").getString("small")))
        imageuris.put("normal", Uri.parse(jsonCard.getJSONObject("image_uris").getString("normal")))
        imageuris.put("large", Uri.parse(jsonCard.getJSONObject("image_uris").getString("large")))
        imageuris.put("png", Uri.parse(jsonCard.getJSONObject("image_uris").getString("png")))
        imageuris.put(
            "art_crop",
            Uri.parse(jsonCard.getJSONObject("image_uris").getString("art_crop"))
        )
        imageuris.put(
            "border_crop",
            Uri.parse(jsonCard.getJSONObject("image_uris").getString("border_crop"))
        )
        return imageuris
    }

    //prepares colors to be loaded into Card
    private fun getColors(jsonCard: JSONObject) : Array<Char> {
        val colorJSONArray = jsonCard.getJSONArray("colors")
        return toCharArray(colorJSONArray)
    }

    //prepares color identity to be loaded into Card
    private fun getColorIdentity(jsonCard: JSONObject) : Array<Char> {
        val colorIDJSONArray = jsonCard.getJSONArray("color_identity")
        return toCharArray(colorIDJSONArray)
    }

    //prepares game mechanic keywords to be loaded into Card
    private fun getKeywords(jsonCard: JSONObject) : Array<String> {
        val keywordJSONArray = jsonCard.getJSONArray("keywords")
        return toStringArray(keywordJSONArray)
    }

    //prepares card finish to be loaded into Card
    private fun getFinishes(jsonCard: JSONObject) : Array<String> {
        val finishJSONArray = jsonCard.getJSONArray("finishes")
        return toStringArray(finishJSONArray)
    }

    //prepares format legality to be loaded into Card
    private fun getLegalities(jsonCard: JSONObject) : HashMap<String, String> {
        val legal: HashMap<String, String> = HashMap<String, String>()
        legal.put("standard", jsonCard.getJSONObject("legalities").getString("standard"))
        legal.put("future", jsonCard.getJSONObject("legalities").getString("future"))
        legal.put("historic", jsonCard.getJSONObject("legalities").getString("historic"))
        legal.put("gladiator", jsonCard.getJSONObject("legalities").getString("gladiator"))
        legal.put("pioneer", jsonCard.getJSONObject("legalities").getString("pioneer"))
        legal.put("explorer", jsonCard.getJSONObject("legalities").getString("explorer"))
        legal.put("modern", jsonCard.getJSONObject("legalities").getString("modern"))
        legal.put("legacy", jsonCard.getJSONObject("legalities").getString("legacy"))
        legal.put("pauper", jsonCard.getJSONObject("legalities").getString("pauper"))
        legal.put("vintage", jsonCard.getJSONObject("legalities").getString("vintage"))
        legal.put("penny", jsonCard.getJSONObject("legalities").getString("penny"))
        legal.put("commander", jsonCard.getJSONObject("legalities").getString("commander"))
        legal.put("brawl", jsonCard.getJSONObject("legalities").getString("brawl"))
        legal.put("historicbrawl", jsonCard.getJSONObject("legalities").getString("historicbrawl"))
        legal.put("alchemy", jsonCard.getJSONObject("legalities").getString("alchemy"))
        legal.put(
            "paupercommander",
            jsonCard.getJSONObject("legalities").getString("paupercommander")
        )
        legal.put("duel", jsonCard.getJSONObject("legalities").getString("duel"))
        legal.put("oldschool", jsonCard.getJSONObject("legalities").getString("oldschool"))
        legal.put("premodern", jsonCard.getJSONObject("legalities").getString("premodern"))
        return legal
    }

    //prepares artist_ids to be loaded into Card
    private fun getArtistIds(jsonCard: JSONObject) : Array<String> {
        return toStringArray(jsonCard.getJSONArray("artist_ids"));
    }

    //prepares prices to be loaded into Card
    private fun getPrices(jsonCard: JSONObject) : HashMap<String, Double?> {
        val prices = HashMap<String, Double?>();
        prices.put("usd", jsonCard.getJSONObject("prices").optDouble("usd"));
        prices.put("usd_foil", jsonCard.getJSONObject("prices").optDouble("usd_foil"));
        prices.put("usd_etched", jsonCard.getJSONObject("prices").optDouble("usd_etched"));
        return prices
    }

    //prepares uris related to card to be loaded into Card
    private fun getRelatedUris(jsonCard: JSONObject) : HashMap<String, Uri?> {
        val related_uris = HashMap<String, Uri?>();
        related_uris.put(
            "gatherer",
            Uri.parse(jsonCard.getJSONObject("related_uris").getString("gatherer"))
        )
        related_uris.put(
            "tcgplayer_infinite_articles",
            Uri.parse(
                jsonCard.getJSONObject("related_uris").getString("tcgplayer_infinite_articles")
            )
        )
        related_uris.put(
            "tcgplayer_infinite_decks",
            Uri.parse(jsonCard.getJSONObject("related_uris").getString("tcgplayer_infinite_decks"))
        )
        related_uris.put(
            "edhrec",
            Uri.parse(jsonCard.getJSONObject("related_uris").getString("edhrec"))
        )
        return related_uris
    }

    //prepares purchase link to be loaded into Card
    private fun getPurchaseUris(jsonCard: JSONObject) : HashMap<String, Uri?> {
        val purchase_uris = HashMap<String, Uri?>();
        purchase_uris.put(
            "tcgplayer",
            Uri.parse(jsonCard.getJSONObject("purchase_uris").getString("tcgplayer"))
        );
        purchase_uris.put(
            "cardmarket",
            Uri.parse(jsonCard.getJSONObject("purchase_uris").getString("cardmarket"))
        );
        purchase_uris.put(
            "cardhoarder",
            Uri.parse(jsonCard.getJSONObject("purchase_uris").getString("cardhoarder"))
        );
        return purchase_uris
    }
}