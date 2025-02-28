package lab1.domain;

import org.junit.Test;
import static org.junit.Assert.*;

public class DomainModelTest {

    @Test
    public void testHouseCreationAndDestruction() {
        House house = new House("123 Main St");
        assertFalse(house.isDestroyed());
        assertNull(house.getDebris());
        assertEquals("123 Main St", house.getAddress());

        house.destroy();
        assertTrue(house.isDestroyed());
        assertNotNull(house.getDebris());
        assertEquals(house, house.getDebris().getOriginHouse());
    }

    @Test
    public void testHouseContents() {
        House house = new House("42 Wallaby Way, Sydney");
        house.addContent("Sofa");
        house.addContent("TV");
        assertEquals(2, house.getContents().size());
        assertTrue(house.getContents().contains("Sofa"));

        house.removeContent("Sofa");
        assertEquals(1, house.getContents().size());
        assertFalse(house.getContents().contains("Sofa"));
    }

    @Test
    public void testArthurRunToHouse() {
        Arthur arthur = new Arthur();
        House house = new House("Some Address");
        arthur.runTo(house);
        assertEquals(house, arthur.getTargetHouse());
    }

    @Test
    public void testArthurNoticing() {
        Arthur arthur = new Arthur();
        assertTrue(arthur.isNoticing()); // Изначально замечает
        arthur.setNoticing(false);
        assertFalse(arthur.isNoticing());
        arthur.setNoticing(true);
        assertTrue(arthur.isNoticing());
    }
      @Test
    public void testArthurReactToWeather() {
        Arthur arthur = new Arthur();
        Weather weather = new Weather();

        // Изначально Arthur замечает все
        assertTrue(arthur.isNoticing());

        // Если погода не меняется, Arthur все еще замечает
        arthur.reactToWeather(weather);
        assertTrue(arthur.isNoticing());

        // Если становится холодно, Arthur перестает замечать
        weather.setCold(true);
        arthur.reactToWeather(weather);
        assertFalse(arthur.isNoticing());

        // Сбрасываем состояние и проверяем с ветром
        arthur.setNoticing(true);
        weather.setCold(false);
        weather.setWindy(true);
        arthur.reactToWeather(weather);
        assertFalse(arthur.isNoticing());

        // Сбрасываем, проверяем с дождем
        arthur.setNoticing(true);
        weather.setWindy(false);
        weather.setRainy(true);
        arthur.reactToWeather(weather);
        assertFalse(arthur.isNoticing());
    }

    @Test
    public void testArthurReactToHouseDestruction(){
        Arthur arthur = new Arthur();
        assertTrue(arthur.isNoticing());
        arthur.reactToHouseDestruction();
        assertFalse(arthur.isNoticing());
    }

    @Test
    public void testWeatherSettersAndGetters() {
        Weather weather = new Weather();
        assertFalse(weather.isCold());
        assertFalse(weather.isWindy());
        assertFalse(weather.isRainy());

        weather.setCold(true);
        assertTrue(weather.isCold());
        weather.setWindy(true);
        assertTrue(weather.isWindy());
        weather.setRainy(true);
        assertTrue(weather.isRainy());
    }
      @Test
    public void testWeatherConstructor() {
        Weather weather = new Weather(true, false, true);
        assertTrue(weather.isCold());
        assertFalse(weather.isWindy());
        assertTrue(weather.isRainy());
    }

    @Test
    public void testBulldozerMoveOverDebris() {
        House house = new House("Some Address");
        house.destroy(); // Создаем обломки
        Debris debris = house.getDebris();
        Bulldozer bulldozer = new Bulldozer();
        bulldozer.moveOver(debris); // Просто вызываем, проверяем, что нет ошибок
    }

    @Test
    public void testBulldozerDestroyHouse() {
        Bulldozer bulldozer = new Bulldozer();
        House house = new House("Another Address");

        assertFalse(house.isDestroyed()); // Дом еще не разрушен
        bulldozer.destroy(house);       // Бульдозер разрушает дом
        assertTrue(house.isDestroyed());  // Дом разрушен
        assertNotNull(house.getDebris()); // Появились обломки

        // Повторное разрушение не должно приводить к ошибке
        bulldozer.destroy(house); // Проверяем, что повторный вызов не ломает
    }

    @Test
    public void testDebrisCreation() {
        House house = new House("Yet Another Address");
        house.destroy();
        Debris debris = house.getDebris();
        assertNotNull(debris);
        assertEquals(house, debris.getOriginHouse()); // Проверяем связь с домом
    }   

    @Test(expected = IllegalStateException.class)
    public void testDestroyAlreadyDestroyedHouse() {
        House house = new House("Test");
        house.destroy();
        house.destroy(); // Должно бросить исключение
    }

    @Test
    public void testFullScenario() {
        // 1. Создание объектов
        House house = new House("123 Main St");
        Arthur arthur = new Arthur();
        Bulldozer bulldozer = new Bulldozer();
        Weather weather = new Weather();

        // 2. Артур бежит к дому
        arthur.runTo(house);
        assertEquals(house, arthur.getTargetHouse());

        // 3. Меняем погоду
        weather.setCold(true);
        weather.setWindy(true);
        weather.setRainy(true);
        
        // 4. Артур реагирует на погоду
        arthur.reactToWeather(weather);
        assertFalse(arthur.isNoticing());

        // 5. Бульдозер разрушает дом
        bulldozer.destroy(house);
        assertTrue(house.isDestroyed());

        // 6. Артур видит разрушение
        arthur.reactToHouseDestruction();
        assertFalse(arthur.isNoticing());

        // 7. Бульдозер перемещается по обломкам
        bulldozer.moveOver(house.getDebris());
        
        // 8. Проверка конечного состояния
        assertNotNull(house.getDebris());
        assertEquals(house, house.getDebris().getOriginHouse());
    }
}